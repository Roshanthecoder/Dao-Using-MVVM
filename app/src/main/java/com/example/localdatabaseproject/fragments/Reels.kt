package com.example.localdatabaseproject.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.localdatabaseproject.R
import com.example.localdatabaseproject.databinding.FragmentReelsBinding
import com.example.localdatabaseproject.utils.CommonUtils
import com.example.localdatabaseproject.viewmodel.ApiViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Reels : Fragment(R.layout.fragment_reels) {

    private lateinit var binding: FragmentReelsBinding
    private lateinit var player: SimpleExoPlayer
    private val models by viewModels<ApiViewModel>()
    private lateinit var playerView: PlayerView
    private var moreReels = ""
    private val TAG = "rOSHAN"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReelsBinding.bind(view)
        initSetView()
        initObserver()
        initListeners()


    }

    private fun initSetView() {
        player = SimpleExoPlayer.Builder(requireContext()).build()

    }

    private fun initHideSearchLay() {
        binding.apply {
            searchLay.visibility = View.GONE
            playerView.visibility = View.VISIBLE
        }
    }


    private fun initShowSearchLay() {
        binding.apply {
            playerView.visibility = View.GONE
            searchLay.visibility = View.VISIBLE
        }
    }


    private fun initListeners() {
        binding.btnSearch.setOnClickListener {
            val userKeyword = binding.edtSearch.text.toString()
            if (userKeyword.isNotEmpty()) {
                models.getUserIdFromUser(userKeyword)
                binding.btnSearch.isEnabled = false
            }
        }
    }

    private fun initObserver() {
        models.reelsResult.observe(viewLifecycleOwner) {
            Log.e(TAG, "list : $it")
            if (it?.status == "ok") {
                binding.btnSearch.isEnabled = false
                initPlayerSetView()
                initHideSearchLay()
            } else {
                initShowSearchLay()
                binding.btnSearch.isEnabled = true
            }
        }
        models.loader.observe(viewLifecycleOwner) {
            binding.loader.visibility = if (it) View.VISIBLE else View.GONE
        }
        models.userID.observe(viewLifecycleOwner) { userIdResult ->
            userIdResult?.let {
                if (it.status == "ok") {
                    it.data?.id
                        ?.toInt()?.let { it1 ->
                            models.fetchInstaReels(
                                userID = it1,
                                maxId = moreReels,
                                noCorse = true
                            )
                        }
                } else {

                    binding.btnSearch.isEnabled = true
                    CommonUtils.showtoast(requireContext(), "Username doesnt exist or incorrect")
                }
            }
            Log.e(TAG, "userId: ${userIdResult?.status}")
        }
    }

    private fun initPlayerSetView() {
        playerView = binding.playerView
        playerView.player = player

        // Prepare a media item
        val mediaItem = MediaItem.fromUri(
            Uri.parse("https://phosphor.ivanenko.workers.dev/?url=https%3A%2F%2Fscontent.cdninstagram.com%2Fo1%2Fv%2Ft16%2Ff2%2Fm69%2FAn87ZkAKQbJhv3qEFJz0p8nVtXVfaV_sx2cJowb7quArrTflYuK47ta_3rxmGRIRZK2jKXgEeH1wgWuWoE9IAT1H.mp4%3Fefg%3DeyJxZV9ncm91cHMiOiJbXCJpZ193ZWJfZGVsaXZlcnlfdnRzX290ZlwiXSIsInZlbmNvZGVfdGFnIjoidnRzX3ZvZF91cmxnZW4uY2xpcHMuYzIuMTA4MC5iYXNlbGluZSJ9%26_nc_cat%3D108%26vs%3D1246047356762843_3706776395%26_nc_vs%3DHBksFQIYOnBhc3N0aHJvdWdoX2V2ZXJzdG9yZS9HQmd2RkFNQ05SSG9pS0lEQUVqRjVZTnJ2U2tMYnBSMUFBQUYVAALIAQAVAhg6cGFzc3Rocm91Z2hfZXZlcnN0b3JlL0dDUzFQaHRsWi1UN1FkNENBS1kzcTgwS2xQb0picV9FQUFBRhUCAsgBACgAGAAbABUAACaatdWQ876MQBUCKAJDMywXQCLEGJN0vGoYFmRhc2hfYmFzZWxpbmVfMTA4MHBfdjERAHX%252BBwA%253D%26_nc_rid%3D7f20d45756%26ccb%3D9-4%26oh%3D00_AYAN3Efiv0y_514sPNEKPOXxjtTlLiJp2PJxLGIL3B1OZQ%26oe%3D66CF35DD%26_nc_sid%3Dc024bc")
        )
        player.setMediaItem(mediaItem)

        // Prepare and start the player
        player.prepare()
        player.playWhenReady = true

        // Set full-screen mode and hide system bars
        requireActivity().window.apply {
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            statusBarColor = resources.getColor(android.R.color.black)
            navigationBarColor = resources.getColor(android.R.color.black)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        // Release the player when done
        player.release()
    }
}
