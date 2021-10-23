package com.appat.blurkittest

import android.animation.ObjectAnimator
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.appat.blurkittest.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    private val maxRadius = 20
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        val bottomSheetBehavior = BottomSheetBehavior.from(binding?.bottomSheet?.bottomSheet!!)

        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val targetHeight = -1 * bottomSheet.height/2
                blurChildLayout(slideOffset)
                ObjectAnimator.ofFloat(binding?.childLayout, "translationY",
                    slideOffset * targetHeight).apply {
                    duration = 0
                    start()
                }
            }
        }
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        binding?.floatingButton?.setOnClickListener {
            openDialog()
        }
    }

    private fun openDialog()
    {
        val dialog = CustomBottomSheetDialog(this)
        dialog.show(supportFragmentManager, "CustomBottomSheetDialog")
    }

    private fun blurChildLayout(percentage: Float)
    {
        if(percentage == 0f)
        {
            removeBlur()
        }
        else
        {
            addBlurToChildLayout(percentage)
        }
    }

    private fun removeBlur()
    {
        if (Build.VERSION.SDK_INT >= 31) {
            binding?.childLayout?.setRenderEffect(null)
        }
    }

    private fun addBlurToChildLayout(percentage: Float)
    {
        if (Build.VERSION.SDK_INT >= 31) {
            coroutineScope.launch {
                val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                    createRenderEffect(maxRadius * percentage)
                }
                val renderEffect = originalDeferred.await()
                binding?.childLayout?.post {
                    binding?.childLayout?.setRenderEffect(renderEffect)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun createRenderEffect(radius: Float): RenderEffect {
        return RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.MIRROR)
    }
}