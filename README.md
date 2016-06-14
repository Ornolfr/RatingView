# RatingView
Simple android widget that can replace standard inconvenient RatingBar in your app.
![]({{site.baseurl}}/https://lh5.googleusercontent.com/21Ct-RgdrYVhzsBf_mV9JP6F_zmaPlFi6U2yxosa9Vi4DCsufBTTLUuuj6C7JYvoo_MAHw=w1896-h859)
##Description
The default Android `RatingBar` widget hardly can satisfy developers' needs. It's a pain to customize it at all. This simple view can take a huge advantage of setting and scaling drawables for rating view easily.
##Usage
You can download this library with the following line in your `app` module `build.gradle`:
```gradle
compile 'com.github.ornolfr:rating-view:0.1.0@aar'
```    
And this line in your project's `build.gradle` :
```gradle
maven { 
	url 'https://dl.bintray.com/ornolfr/maven/' 
}
```
##Example
Declare `RatingView` in your XML with `app` attributes:
```xml
<com.github.ornolfr.ratingview.RatingView
	android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:drawable_empty="@drawable/ic_star_empty"
    app:drawable_filled="@drawable/ic_star_filled"
    app:drawable_half="@drawable/ic_star_half"
    app:drawable_margin="4dp"
    app:drawable_size="24dp"
    app:is_indicator="false"
    app:max_count="5"
    app:rating="3.5" />
```
And use it through `RatingView` instance in your code. Goog luck!