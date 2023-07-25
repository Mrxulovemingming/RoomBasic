package com.example.words

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation


class AddFragment : Fragment() {
    private lateinit var wordViewModel: WordViewModel
    lateinit var buttonSubmit: Button
    lateinit var editTextEnglish: EditText
    lateinit var editTextChinese: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)
        editTextEnglish = view.findViewById(R.id.editTextEnglish)
        editTextChinese = view.findViewById(R.id.editTextChinese)
        buttonSubmit.isEnabled = false
        editTextEnglish.requestFocus()
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        // 显示键盘，并将光标聚焦在 editTextEnglish 上
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editTextEnglish, 0)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val english = editTextEnglish.text.toString().trim()
                val chinese = editTextChinese.text.toString().trim()
                buttonSubmit.isEnabled = (english.isNotEmpty() && chinese.isNotEmpty())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editTextEnglish.addTextChangedListener(textWatcher)
        editTextChinese.addTextChangedListener(textWatcher)
        buttonSubmit.setOnClickListener {
            val english = editTextEnglish.text.toString().trim()
            val chinese = editTextChinese.text.toString().trim()
            wordViewModel.addWord(Word(english, chinese))
            val navController = Navigation.findNavController(it)
            /*
            * 如果 Navigate 到 WordsFragment ，里面的 RecycleView 是新的，也就不存在 itemInserted 动画
            * 因此必须使用 NavigateUp
            * */
            navController.navigateUp()
        }
    }


}