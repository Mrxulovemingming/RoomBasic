package com.example.words

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.view.View

import androidx.recyclerview.widget.RecyclerView

import android.graphics.Canvas

import android.graphics.Color

import android.graphics.drawable.ColorDrawable

import android.graphics.drawable.Drawable

import androidx.core.content.ContextCompat





class WordsFragment : Fragment() {
    lateinit var wordViewModel: WordViewModel
    lateinit var recyclerView: RecyclerView
    lateinit var wordAdapter: WordAdapter
    private lateinit var floatingActionButton: FloatingActionButton
    lateinit var filteredWords: LiveData<List<Word>>
    lateinit var allWords: List<Word>
    private lateinit var dividerItemDecoration: DividerItemDecoration

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
        when (item.itemId) {
            R.id.clear_data -> {
                val alertDialog = AlertDialog.Builder(requireActivity())
                alertDialog.setTitle("清空数据")
                alertDialog.setPositiveButton("确定") { _, _ ->
                    wordViewModel.clearWords()
                }
                alertDialog.setNegativeButton("取消") { _, _ ->
                }
                alertDialog.create()
                alertDialog.show()
            }
            R.id.switchViewType -> {
                Toast.makeText(requireActivity(), "1111", Toast.LENGTH_LONG).show()
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
        // <editor-fold defaultstate="collapsed" desc="解决标签没有被刷新的问题">
        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                super.onAnimationFinished(viewHolder)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                for (i in firstPosition..lastPosition) {
                    val holder =
                        recyclerView.findViewHolderForAdapterPosition(i) as WordAdapter.WordViewHolder
                    holder.textViewNumber.text = (i + 1).toString()
                }
            }
        }
        // </editor-fold>

        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        wordAdapter = WordAdapter(wordViewModel)
        recyclerView.adapter = wordAdapter
        // <editor-fold defaultstate="collapsed" desc="加上滑动分隔线">
        dividerItemDecoration =
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        // </editor-fold>

        filteredWords = wordViewModel.getAllWords()
        filteredWords.observe(requireActivity()) {
            allWords = filteredWords.value!!
            recyclerView.smoothScrollBy(0, -2000) // 回到顶部，防止当元素已经很多时，插入动画无法展示
            wordAdapter.submitList(it)
        }
        floatingActionButton.setOnClickListener {
            val controller = Navigation.findNavController(it)
            controller.navigate(R.id.action_wordsFragment_to_addFragment)
        }
        // <editor-fold defaultstate="collapsed" desc="滑动删除功能实现">
        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                ItemTouchHelper.START or ItemTouchHelper.END
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val wordFrom = allWords[viewHolder.adapterPosition]
                val wordTo = allWords[target.adapterPosition]
                val idTemp = wordFrom.id
                wordFrom.id = wordTo.id
                wordTo.id = idTemp
                wordViewModel.updateWord(wordFrom, wordTo)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val wordToDelete = allWords[viewHolder.adapterPosition]
                wordViewModel.deleteSingleWord(wordToDelete)
                // <editor-fold defaultstate="collapsed" desc="SnackBar 来撤销删除的单词">
                Snackbar.make(
                    requireActivity().findViewById(R.id.wordsFragmentView),
                    "删除了一个单词",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("撤销") {
                        wordViewModel.addWord(wordToDelete)
                    }.show()
                // </editor-fold>
            }
            // <editor-fold defaultstate="collapsed" desc="绘制滑动删除背后的垃圾桶">
            var icon = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_baseline_delete_forever_24
            )
            var background: Drawable = ColorDrawable(Color.LTGRAY)
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                val iconLeft: Int
                val iconRight: Int
                val iconBottom: Int
                val backLeft: Int
                val backRight: Int
                val backTop: Int = itemView.top
                val backBottom: Int = itemView.bottom
                val iconTop: Int = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
                iconBottom = iconTop + icon!!.intrinsicHeight
                when {
                    dX > 0 -> {
                        backLeft = itemView.left
                        backRight = itemView.left + dX.toInt()
                        background.setBounds(backLeft, backTop, backRight, backBottom)
                        iconLeft = itemView.left + iconMargin
                        iconRight = iconLeft + icon!!.intrinsicWidth
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    }
                    dX < 0 -> {
                        backRight = itemView.right
                        backLeft = itemView.right + dX.toInt()
                        background.setBounds(backLeft, backTop, backRight, backBottom)
                        iconRight = itemView.right - iconMargin
                        iconLeft = iconRight - icon!!.intrinsicWidth
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    }
                    else -> {
                        background.setBounds(0, 0, 0, 0)
                        icon!!.setBounds(0, 0, 0, 0)
                    }
                }
                background.draw(c)
                icon!!.draw(c)
            }
            // </editor-fold>
        }).attachToRecyclerView(recyclerView)
        // </editor-fold>

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
                val oldWords = filteredWords
                if (!newText.isNullOrEmpty()) {
                    if (newText.trim().isNotEmpty()) {
                        filteredWords = wordViewModel.getSearchWord(newText.trim())
                    }
                } else {
                    filteredWords = wordViewModel.getAllWords()
                }
                oldWords.removeObservers(requireActivity()) // 移除旧的观察者
                filteredWords.observe(requireActivity()) { words ->
                    allWords = filteredWords.value!!
                    wordAdapter.submitList(words)
                }
                return true
            }
        })
    }


}