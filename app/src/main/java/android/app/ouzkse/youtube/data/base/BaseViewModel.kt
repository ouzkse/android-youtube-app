package android.app.ouzkse.youtube.data.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    lateinit var serviceFetchingJob: Job

    fun isFetching() = serviceFetchingJob.isActive
}