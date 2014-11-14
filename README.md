Automatic Sliding ViewPager
==========================

## Sample Application
![Screenshot](https://github.com/Caij/Android-AutoSlideViewPager/raw/master/sample.gif)

## Usage
- include this library, use

``` xml
<com.android.caij.autoslideviewpager.AutoSlideViewPager
	android:id="@+id/view_pager"
	android:layout_width="match_parent"
	android:layout_height="wrap_content" />
```
- `startSlide()` start auto slide.
- `stopSlide()` stop auto Slide.

## Setting
- `setIntervalTime(int) `set auto slide time in milliseconds, default is `INTERVAL_TIME`.
- `setStopScrollWhenTouch(boolean)` set whether stop auto slide when touching, default is true.
- `setShowTitle(boolean) ` Whether the display page title, default is true.
- `setShowPoint(boolean)   ` Whether the display point images, default is true.

## Contact Me

- [worldcaij#gmail.com](mailto:worldcaij@gmail.com)