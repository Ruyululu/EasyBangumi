package com.heyanle.easybangumi4.cartoon.story.download.step

import androidx.annotation.WorkerThread
import com.heyanle.easybangumi4.cartoon.story.download.runtime.CartoonDownloadRuntime

/**
 * Created by heyanle on 2024/7/7.
 * https://github.com/heyanLE
 */
interface BaseStep {


    @WorkerThread
    fun invoke()

    fun cancel(runtime: CartoonDownloadRuntime)

}