package com.iti.team.ecommerce.utils

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iti.team.ecommerce.R
import com.iti.team.ecommerce.utils.extensions.getParentActivity
import com.smarteist.autoimageslider.SliderViewAdapter

/**
 * Set [TextView] text.
 *
 * @param titleDetails: [MutableLiveData] of List<[Any]>
 *     List[0] -> Title string id (Integer).
 *     List[1] -> Title color id (Integer).
 *     List[2] -> Is title bold (Boolean).
 *     List[3] -> Details (String).
 *     List[4] -> Details color id (Integer).
 *     List[5] -> Is details bold (Boolean).
 */
@BindingAdapter(
    "mutableText",
    "mutableTextId",
    "mutableTitleDetails",
    "mutableSpannableStringBuilder",
    "mutableAlignment",
    requireAll = false
)
fun setMutableText(
    view: TextView,
    @Nullable text: LiveData<String>?,
    @Nullable textId: LiveData<Int>?,
    @Nullable titleDetails: LiveData<List<Any>>?,
    @Nullable spannableStringBuilder: LiveData<SpannableStringBuilder>?,
    @Nullable alignment:LiveData<Int>?
) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        text?.let { text.observe(parentActivity, Observer { value -> view.text = value ?: "" }) }

        textId?.let {
            textId.observe(
                parentActivity,
                Observer { value -> value?.let { view.setText(value) } })
        }

        alignment?.let { alignment.observe(parentActivity,
            Observer { value ->
                view.textAlignment = value ?: 0
            })
        }

        titleDetails?.let {
            titleDetails.observe(parentActivity, Observer { value ->
                Log.i("BindingAdapter","Setting title details data with data: $value")

                // Title.
                val spannableTitle = SpannableString("${view.context.getString(value[0] as Int)}: ")

                spannableTitle.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, value[1] as Int)),
                    0,
                    spannableTitle.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                if (value[2] as Boolean) {
                    spannableTitle.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        spannableTitle.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                // Details.
                val spannableDetails = SpannableString(value[3] as String)

                spannableDetails.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(view.context, value[4] as Int)),
                    0,
                    spannableDetails.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                if (value[5] as Boolean) {
                    spannableDetails.setSpan(
                        StyleSpan(Typeface.BOLD),
                        0,
                        spannableDetails.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                // Set text to text view.
                view.text = TextUtils.concat(spannableTitle, spannableDetails)
            })
        }

        spannableStringBuilder?.let {
            spannableStringBuilder.observe(
                parentActivity,
                Observer { value -> view.setText(value, TextView.BufferType.SPANNABLE) })
        }
    }
}

/**
 * Set [CompoundButton] text.
 */
@BindingAdapter("mutableText", "mutableTextId",requireAll = false)
fun setMutableText(view: Button, @Nullable text: LiveData<String>?,
                   @Nullable textId: LiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        text?.let { text.observe(parentActivity, Observer { value -> view.text = value ?: "" }) }

        textId?.let {
            textId.observe(
                parentActivity,
                Observer { value -> value?.let { view.setText(value) } })
        }

    }
}

/**
 * Set [CompoundButton] text.
 */
@BindingAdapter("mutableText",requireAll = false)
fun setMutableText(view: SearchView, @Nullable text: LiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        text?.let { text.observe(parentActivity, Observer { value -> view.setQuery(value, false)}) }

    }
}

/**
 * Set [AppCompatEditText] text.
 */
@BindingAdapter("mutableText", "mutableTextId", "textColor",requireAll = false)
fun setMutableText(
    view: AppCompatEditText,
    @Nullable text: LiveData<String>?,
    @Nullable textId: LiveData<Int>?,
    @Nullable color: LiveData<Int>?,
) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null) {
        text?.let { text.observe(parentActivity, Observer { value -> view.setText(value) }) }

        textId?.let {
            textId.observe(
                parentActivity,
                Observer { value -> value?.let { view.setText(value) } })
        }

        color?.let {
            color.observe(
                parentActivity,
                Observer { value -> value?.let { view.setTextColor(value) } })
        }
    }
}


/**
 * Set [RecyclerView] adapter.
 */
@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, @Nullable adapter: RecyclerView.Adapter<*>?) {
    adapter?.let { view.adapter = adapter }
}


/**
 * Set [View] visibility.
 */
@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, @Nullable visibility: LiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()

    if (parentActivity != null && visibility != null) {
        visibility.observe(
            parentActivity,
            Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}
/**
 * Set [CompoundButton] properties.
 */
@BindingAdapter("mutableCheck", requireAll = false)
fun bindingAdapterCompoundButton(view: CompoundButton, @Nullable checked: LiveData<Boolean?>?) {
    view.getParentActivity()?.let { parentActivity ->
        checked?.let { checked ->
            checked.observe(
                parentActivity,
                { value -> value?.let { view.isChecked = it } })
        }
    }
}

@BindingAdapter("mutableSrc","colorImage", requireAll = false)
fun setImage(view:ImageView, @Nullable drawableId: LiveData<Int?>?,@Nullable color:LiveData<Int?>? ){
    view.getParentActivity()?.let { parentActivity ->
        drawableId?.let {
            drawableId->
            drawableId.observe(parentActivity,
            {value-> value?.let{view.setImageResource(it)}})
        }
    }
    view.getParentActivity()?.let { parentActivity ->
        color?.let {
                color->
            color.observe(parentActivity,
                {value-> value?.let{view.setColorFilter(it)}})
        }
    }
}

@BindingAdapter("image")
fun setImageWithGlide(image: ImageView, @Nullable url:  LiveData<String?>?) {

    image.getParentActivity()?.let { parentActivity ->
        url?.let {
                url->
            url.observe(parentActivity,
                {value-> value?.let{
                    Glide.with(image.context).load(it)
                    .placeholder(R.drawable.home)
                    .into(image)}})
        }
    }
}

