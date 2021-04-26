package com.mee.expensetracker.domain

import com.mee.expensetracker.base.CompletableUseCaseWrapper
import com.mee.expensetracker.base.CompletableWrapper
import com.mee.expensetracker.db.AppDatabase
import com.mee.expensetracker.domain.repository.FAuthRepository
import com.mee.expensetracker.model.Expense
import com.mee.expensetracker.model.Income
import dagger.Reusable
import javax.inject.Inject

/**
 * Created by Michelle Dayangco on 3/17/21.
 */
class SignInUseCase @Inject constructor(private val repository: FAuthRepository): CompletableUseCaseWrapper() {

    operator fun invoke(): CompletableWrapper {
        return buildUseCase {
           repository.signin()
        }
    }
}