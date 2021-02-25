package com.example.myapplication.ui.main;

import android.widget.ListView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;

public class MainViewModel extends ViewModel {
    public MutableLiveData<ListView> list;
    public MutableLiveData<java.io.File[]> File;
    public MutableLiveData<String []> Filename;
    public MutableLiveData<Integer> Flag;


    public MainViewModel () {
        list = new MutableLiveData<>();
        File = new MutableLiveData<>();
        Filename = new MutableLiveData<>();
        Flag = new MutableLiveData<>();
        Flag.setValue(0);



    }

    public MutableLiveData<ListView> getList() {
        return list;
    }

    public MutableLiveData<File[]> getFile() {
        return File;
    }

    public MutableLiveData<String []> getFilename() { return Filename;}

    public MutableLiveData<Integer> getFlag() {
        return Flag;
    }}