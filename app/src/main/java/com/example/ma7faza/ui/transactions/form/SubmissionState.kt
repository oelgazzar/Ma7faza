package com.example.ma7faza.ui.transactions.form

sealed interface SubmissionState {
    data object Idle : SubmissionState
    data object Saving: SubmissionState
}