
# Rich Editor Lib
This library is a representation of [Android Rich text Editor](https://github.com/chinalwb/Android-Rich-text-Editor) based on Android native classes. This build offers an optimized library reimplemented in `Kotlin` by using [Material Design Components](https://material.io/components?platform=android).

![lib](https://user-images.githubusercontent.com/77691290/106530874-6fc2db80-64ed-11eb-9e7e-bcc5470cc3e1.jpg)

## Features
Currently, it does not include all features introduced in the [original](https://github.com/chinalwb/Android-Rich-text-Editor) library. But nevertheless, they will be added release by release. Currently available features in [![](https://jitpack.io/v/easy-apps-2018/rich-editor-lib.svg)](https://jitpack.io/#easy-apps-2018/rich-editor-lib):
  - Bold
  - Italic
  - Underline
  - Strikethrough
  - Text size
  - Foreground color
  - Link/Phone numbers/Emails
## Integration
First add this in to root `build.gradle` at the end of repositories :
```groovy
allprojects {
   repositories {
 	...
	maven { url 'https://jitpack.io' }
   }
}
```
Then add this dependency in your apps `build.gradle`:
```groovy
implementation 'com.github.easy-apps-2018:rich-editor-lib:0.1.1'
```
## Usage
In `XML`: `activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.easyapps.richeditorlib.widgets.RichEditText
        android:id="@+id/richEditText"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_margin="16dp"
        android:background="@null"
        app:layout_constraintBottom_toTopOf="@+id/styleBar"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="1.0" />

    <com.easyapps.richeditorlib.widgets.StyleBar
        android:id="@+id/styleBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="1.0" />
</androidx.constraintlayout.widget.ConstraintLayout>
```
In `Kotlin` class: `MainActivity.kt`
```kotlin
...
private lateinit var styleBar: StyleBar
private lateinit var richEditText: RichEditText
...
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    ...
    styleBar = findViewById(R.id.styleBar)
    richEditText = findViewById(R.id.richEditText)
    richEditText.setStyleBar(styleBar) // Set StyleBar to EditText
    styleBar.setEditText(richEditText) // Set EditText to StyleBar
    
    val html = richEditText.getHtml() // Get formatted html string
    ...
    richEditText.setHtml(html) // Set formatted html string
}
...
```
For rendering you can use the `RichTextView` class:
Add it in your `XML`: 
```xml
<com.easyapps.richeditorlib.widgets.RichTextView
    android:id="@+id/richTextView"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:layout_margin="16dp"  
    app:layout_constraintBottom_toBottomOf="parent"  
    app:layout_constraintEnd_toEndOf="parent"  
    app:layout_constraintStart_toStartOf="parent"  
    app:layout_constraintTop_toTopOf="parent" />
```
Set the `html` string content:
```kotlin
val richTextView: RichTextView = findViewById(R.id.richTextView)  
richTextView.setHtml(html) // Set formatted html string
```
## Contribution
If you have any suggestions or found anything, whatever should have an impact on code performance or if you want to provide some improvements, please feel free to open an issue or to contact: easy.app.2018@gmail.com.
Additionally, for specific questions, you can refer to the [original](https://github.com/chinalwb/Android-Rich-text-Editor) library and take a look to some comments in the source code or contact the owner ([chinalwb](https://github.com/chinalwb)) of the library.
## License
This library runs under [Apache-2.0 License](https://github.com/chinalwb/Android-Rich-text-Editor/blob/master/license) for more information please refer to the [original](https://github.com/chinalwb/Android-Rich-text-Editor) library.
