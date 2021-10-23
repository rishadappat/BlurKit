package com.appat.blurkittest

import android.content.DialogInterface
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CustomBottomSheetDialog(private val parent: AppCompatActivity): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        blurActivity()
        super.onCreate(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        removeBlur()
        super.onDismiss(dialog)
    }

    private fun blurActivity()
    {
        if (Build.VERSION.SDK_INT >= 31) {
            parent.window?.setFlags(
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            )
            parent.window?.attributes?.blurBehindRadius = 10
            parent.window?.decorView?.setRenderEffect(RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.MIRROR))
            parent.window?.setBackgroundBlurRadius(10)
        }
    }

    private fun removeBlur()
    {
        if (Build.VERSION.SDK_INT >= 31) {
            parent.window?.setFlags(
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND
            )
            parent.window?.attributes?.blurBehindRadius = 0
            parent.window?.decorView?.setRenderEffect(null)
            parent.window?.setBackgroundBlurRadius(0)
        }
    }
}