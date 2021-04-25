package com.yaroslavgamayunov.stockviewer.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.yaroslavgamayunov.stockviewer.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showRetrySnackbar(
        @StringRes textResId: Int,
        anchorView: View? = null,
        retryAction: () -> Unit
    ) {
        Snackbar.make(findViewById(R.id.contentView), textResId, Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.YELLOW)
            .setAnchorView(anchorView)
            .setAction(R.string.retry) { retryAction() }.show()
    }
}