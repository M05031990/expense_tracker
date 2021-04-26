package  com.mee.expensetracker.base

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.net.UnknownHostException


interface CompositeDisposableContainer {
    fun getCompositeDisposable() : CompositeDisposable
}

open class CompletableWrapper(private val completable: Completable): BaseObserver<Nothing>() {
   private fun subscribe(disposableContainer: CompositeDisposableContainer,callback: Callback<Nothing>){
        this.callback = callback
        completable.subscribe(this)
        disposableContainer.getCompositeDisposable().add(this.disposable)

   }

   fun subscribe(disposableContainer: CompositeDisposableContainer,response: MutableLiveData<RequestResponse<Nothing>>? = null,
                 onProgress: Consumer<in Boolean>? = null,onComplete: Action? = null,
                 onError: Consumer<in String>? = null,
                 onProgressLiveData: MutableLiveData<Boolean>? = null, onErrorLiveData: MutableLiveData<String>? = null,
                 onFinished: Action? = null){
       subscribe(disposableContainer, object : Callback<Nothing>{
           override fun onProgress(progress: Boolean) {
               response?.value = RequestResponse.Progress(progress)
               onProgress?.accept(progress)
               onProgressLiveData?.value = progress
           }

           override fun onSuccess(data: Nothing?) {
               response?.value = RequestResponse.Success(null)
               onComplete?.run()
               onFinished?.run()
           }

           override fun onFailure(error: String) {
               response?.value = RequestResponse.Failure(error)
               onError?.accept(error)
               onErrorLiveData?.value = error
               onFinished?.run()
           }

       })
   }
}
open class SingleWrapper<T : Any> constructor(private val single : Single<T>) : BaseObserver<T>() {
    private fun subscribe(disposableContainer: CompositeDisposableContainer, callback: Callback<T>){
        this.callback = callback
        single.subscribe(this)
        disposableContainer.getCompositeDisposable().add(this.disposable)

    }

    fun subscribe(disposableContainer: CompositeDisposableContainer, response: MutableLiveData<RequestResponse<T>>? = null,
                  onProgress: Consumer<in Boolean>? = null,onSuccess: Consumer<in T>? = null, onError: Consumer<in String>? = null,
                  onProgressLiveData: MutableLiveData<Boolean>? = null, onErrorLiveData: MutableLiveData<String>? = null,
                  onFinished: Action? = null){
        subscribe(disposableContainer, object : Callback<T>{
            override fun onProgress(progress: Boolean) {
                response?.value = RequestResponse.Progress(progress)
                onProgress?.accept(progress)
                onProgressLiveData?.value = progress
            }

            override fun onSuccess(data: T?) {
                response?.value = RequestResponse.Success(data)
                onSuccess?.accept(data)
                onFinished?.run()
            }

            override fun onFailure(error: String) {
                response?.value = RequestResponse.Failure(error)
                onError?.accept(error)
                onErrorLiveData?.value = error
                onFinished?.run()
            }

        })
    }
}
open class CompletableUseCaseWrapper : BaseWrapper() {
    private fun applySingleSchedulers(): CompletableTransformer{
        return CompletableTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
    fun buildUseCase(f: () -> Completable): CompletableWrapper {
        return CompletableWrapper(f().compose(applySingleSchedulers()))
    }
}

open class SingleUseCaseWrapper : BaseWrapper() {
    private fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
    fun <T:Any> buildUseCase(f: () -> Single<T>): SingleWrapper<T> {
        return SingleWrapper(f().compose(applySingleSchedulers()))

    }
}
sealed class BaseUseCaseListener{

}
abstract class BaseWrapper
abstract class BaseObserver<T : Any> : CompletableObserver, SingleObserver<T>{

    var disposable: CompositeDisposable = CompositeDisposable()
    lateinit var callback: Callback<T>

    override fun onComplete() {
        callback.onProgress(false)
        callback.onSuccess(null)
    }

    override fun onSubscribe(d: Disposable) {
        disposable = CompositeDisposable()
        disposable.add(d)
        callback.onProgress(true)
    }
    override fun onError(throwable: Throwable) {
        throwable.printStackTrace()
        callback.onProgress(false)
        callback.onFailure(throwable.localizedMessage)
    }

    override fun onSuccess(t: T) {
        callback.onProgress(false)
        callback.onSuccess(t)
    }
    interface Callback<T>{
        fun onProgress(progress: Boolean)
        fun onSuccess(data: T?)
        fun onFailure(error: String)
    }
}