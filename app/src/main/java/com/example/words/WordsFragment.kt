package com.example.words

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WordsFragment : Fragment() {
    lateinit var wordViewModel: WordViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var wordAdapter: WordAdapter
    lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    // onViewCreated 用于替换 onActivityCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        recyclerView = view.findViewById(R.id.recycleview)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        wordAdapter = WordAdapter(wordViewModel)
        recyclerView.adapter = wordAdapter
        wordViewModel.getAllWords().observe(requireActivity()) {
            val temp = wordAdapter.itemCount
            wordAdapter.setAllWords(it)
            if (temp != it.size) {
                wordAdapter.notifyDataSetChanged()
            }
        }
        floatingActionButton.setOnClickListener {
            val controller = Navigation.findNavController(it)
            controller.navigate(R.id.action_wordsFragment_to_addFragment)
        }
    }


}