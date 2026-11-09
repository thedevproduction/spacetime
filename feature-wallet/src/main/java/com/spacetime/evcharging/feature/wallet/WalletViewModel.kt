package com.spacetime.evcharging.feature.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacetime.evcharging.core.common.AppResult
import com.spacetime.evcharging.data.model.Wallet
import com.spacetime.evcharging.data.model.WalletTransaction
import com.spacetime.evcharging.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WalletUiState(
    val wallet: Wallet? = null,
    val transactions: List<WalletTransaction> = emptyList(),
    val filteredTransactions: List<WalletTransaction> = emptyList(),
    val selectedFilter: String = "ALL", // ALL, TOPUP, CHARGE, REFUND
    val topUpAmountText: String = "500",
    val selectedPaymentMethod: String = "UPI", // UPI, CARD, NETBANKING
    val isLoading: Boolean = true,
    val isProcessingPayment: Boolean = false,
    val paymentSuccessRef: String? = null,
    val error: String? = null
)

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val walletRepository: WalletRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    init {
        loadWalletData()
    }

    fun loadWalletData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val walletRes = walletRepository.getWallet()
            val txRes = walletRepository.getTransactions()

            if (walletRes is AppResult.Success && txRes is AppResult.Success) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        wallet = walletRes.data,
                        transactions = txRes.data,
                        filteredTransactions = filterTransactions(txRes.data, it.selectedFilter)
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load wallet data") }
            }
        }
    }

    fun setTopUpAmount(amount: String) {
        _uiState.update { it.copy(topUpAmountText = amount) }
    }

    fun selectPaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun selectFilter(filter: String) {
        _uiState.update {
            it.copy(
                selectedFilter = filter,
                filteredTransactions = filterTransactions(it.transactions, filter)
            )
        }
    }

    fun processTopUp(onSuccess: () -> Unit) {
        val amount = _uiState.value.topUpAmountText.toLongOrNull() ?: return
        if (amount <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingPayment = true, error = null) }
            val amountMinor = amount * 100
            when (val res = walletRepository.createTopUp(amountMinor)) {
                is AppResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            paymentSuccessRef = res.data
                        )
                    }
                    loadWalletData()
                    onSuccess()
                }
                is AppResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isProcessingPayment = false,
                            error = res.message
                        )
                    }
                }
            }
        }
    }

    fun clearPaymentSuccess() {
        _uiState.update { it.copy(paymentSuccessRef = null) }
    }

    private fun filterTransactions(list: List<WalletTransaction>, filter: String): List<WalletTransaction> {
        return when (filter) {
            "TOPUP" -> list.filter { it.amount.amountMinor > 0 }
            "CHARGE" -> list.filter { it.amount.amountMinor < 0 }
            "REFUND" -> list.filter { it.type.contains("Cashback", ignoreCase = true) || it.type.contains("Refund", ignoreCase = true) }
            else -> list
        }
    }
}
