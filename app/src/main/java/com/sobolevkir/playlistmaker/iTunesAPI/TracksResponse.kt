package com.sobolevkir.playlistmaker.iTunesAPI

import com.sobolevkir.playlistmaker.tracklist.Track

class TracksResponse (val resultCount: Int,
                      val results: ArrayList<Track>){
}