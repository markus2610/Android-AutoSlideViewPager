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
- `setScrollDurationFactor(double) ` set the factor by which the multiple of duration of sliding animation will change, default 1.0.

## Contact Me

- [worldcaij#gmail.com](mailto:worldcaij@gmail.com)

## License

    Copyright 2014 Caij

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.