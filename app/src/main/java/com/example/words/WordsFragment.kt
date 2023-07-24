package com.example.words

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
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
    lateinit var filteredWords: LiveData<List<Word>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    init {
        setHasOptionsMenu(true) // 必须加上这句话，不然 menu 无法显示
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.clear_data ->{
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.setTitle("清空数据")
                alertDialog.setPositiveButton("确定"){
                    _,_ -> wordViewModel.clearWords()
                }
                alertDialog.setNegativeButton("取消"){
                    _,_ ->
                }
                alertDialog.create()
                alertDialog.show()
            }
            R.id.switchViewType->{
                Toast.makeText(requireActivity(),"1111",Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
        filteredWords = wordViewModel.getAllWords()
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

    // 添加 menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu, menu)
        // 防止搜索框出现将 actionBar 的标题挤占掉
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.maxWidth = 730
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText.trim().isNotEmpty()) {
                        filteredWords = wordViewModel.getSearchWord(newText.trim())
                        filteredWords.observe(requireActivity()){
                            val temp = wordAdapter.itemCount
                            wordAdapter.setAllWords(it)
                            if (temp != it.size) {
                                wordAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                return true
            }

        })
    }


}