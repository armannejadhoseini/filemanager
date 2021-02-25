package com.example.myapplication.ui.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.apache.commons.io.FileUtils;


import com.example.myapplication.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class MainFragment extends Fragment {

    private MainViewModel dashboardViewModel;
    private File[] listOfFiles;
    private ListView lister;
    private File defaultpath;
    private String[] filesname;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main_fragment, container, false);

        lister = root.findViewById(R.id.listview1);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dashboardViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        checkStorageReadPermission();
        checkStorageWritePermission();
    }


    private void filefinder(File defaultpath) {
        listOfFiles = defaultpath.listFiles();
        dashboardViewModel.getFile().setValue(listOfFiles);
        filesname = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            filesname[i] = listOfFiles[i].getName();
        }
        dashboardViewModel.getFlag().setValue(1);
        dashboardViewModel.getFilename().setValue(filesname);
    }

    private void filefinder2(File defaultpath) {
        listOfFiles = defaultpath.listFiles();
        assert listOfFiles != null;
        filesname = new String[listOfFiles.length];
        for (int i = 0; i < listOfFiles.length; i++) {
            filesname[i] = listOfFiles[i].getName();
        }
    }

    private void customadaptor() {

        CustomAdaptor folderlist = new CustomAdaptor(getActivity(), dashboardViewModel.getFilename().getValue(), Objects.requireNonNull(dashboardViewModel.getFile().getValue()));

        lister.setAdapter(folderlist);
    }

    private void customadaptor2(ListView foldername) {

        CustomAdaptor folderlist = new CustomAdaptor(getActivity(), filesname, listOfFiles);

        foldername.setAdapter(folderlist);
    }


    @SuppressLint("WrongConstant")
    private void fileopener(File file) {
        Intent myIntent = new Intent("android.intent.action.VIEW");
        myIntent.setDataAndType(Uri.fromFile(file), MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())));
        startActivity(myIntent);
    }


    private void infoadaptor(String[] info, ListView foldername) {
        File[] f = new File[1];
        f[0] = new File("infoadaptor");
        foldername.setAdapter(new CustomAdaptor(getActivity(), info, f));
    }


    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
        super.onCreate(savedInstanceState);
    }

    public void onCreateOptionsMenu(@androidx.annotation.NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.topactionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onCreateContextMenu(@androidx.annotation.NonNull ContextMenu menu, @androidx.annotation.NonNull View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        new MenuInflater(getContext()).inflate(R.menu.droplist, menu);
        menu.setHeaderTitle("Select Action");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            dialog1(listOfFiles[0].getParent());
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onContextItemSelected(MenuItem item) {
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        if (item.getItemId() == R.id.one) {
            File file = new File(String.valueOf(listOfFiles[position]));
            if (file.isDirectory()) {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                boolean ok = file.delete();
                if (ok) {
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                }
            }
            customadaptor();
        }
        if (item.getItemId() == R.id.two) {
            dialog4(listOfFiles[position].getAbsolutePath(), listOfFiles[position].getParent());
        }
        if (item.getItemId() == R.id.three) {
            dialog2(listOfFiles[position].getParent(), listOfFiles[position].getName());
        }

        if (item.getItemId() == R.id.four) {
            dialog5(listOfFiles[position].getParent(), listOfFiles[position].getName());
        }

        if (item.getItemId() == R.id.five) {
            dialog3(main(listOfFiles[((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position]));
        }
        return true;
    }

    private void dialog1(final String name) {
        AlertDialog.Builder mkfolder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialog = getLayoutInflater().inflate(R.layout.dialogframe, null);
        final EditText foldername = dialog.findViewById(R.id.namepicker);
        mkfolder.setView(dialog);
        final AlertDialog dialog1 = mkfolder.create();
        dialog1.show();
        Button save = dialog.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                File f2 = new File(name + "/" + foldername.getText().toString());
                if (!f2.exists()) {
                    boolean ok = f2.mkdirs();
                    if (ok) {
                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                    }
                }
                customadaptor();
                dialog1.dismiss();
            }
        });
    }


    private void dialog2(final String m, final String n) {
        AlertDialog.Builder mkfolder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialog = getLayoutInflater().inflate(R.layout.dialogframe2, null);
        final ListView foldername = dialog.findViewById(R.id.listview1);

        filefinder2(defaultpath);

        customadaptor2(foldername);

        foldername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                File[] file = new File[1];
                file[0] = listOfFiles[position].getAbsoluteFile();
                listOfFiles = file;
                File file1 = new File(listOfFiles[0].getAbsolutePath());
                filefinder2(file1);
                customadaptor2(foldername);
            }
        });
        mkfolder.setView(dialog);
        final AlertDialog dialog1 = mkfolder.create();
        dialog1.show();
        Button saver = dialog.findViewById(R.id.saver);
        saver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String path = listOfFiles[0].getParent();
                try {
                    File s10 = new File(m + "/" + n);
                    File s20 = new File(path + "/" + n);
                    copyFileUsingStream(s10, s20);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog1.dismiss();
                customadaptor();
            }
        });
    }

    private void dialog3(String[] info) {
        AlertDialog.Builder mkfolder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialog = getLayoutInflater().inflate(R.layout.dialogframe2, null);
        infoadaptor(info, (ListView) dialog.findViewById(R.id.listview1));
        mkfolder.setView(dialog);
        final AlertDialog dialog1 = mkfolder.create();
        dialog1.show();
        Button saver = dialog.findViewById(R.id.saver);
        saver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void dialog4(final String s1, final String m) {
        AlertDialog.Builder mkfolder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialog = getLayoutInflater().inflate(R.layout.dialogframe, null);
        final EditText foldername = dialog.findViewById(R.id.namepicker);
        mkfolder.setView(dialog);
        AlertDialog dialog1 = mkfolder.create();
        dialog1.show();
        Button saver = dialog.findViewById(R.id.save);
        final AlertDialog alertDialog = dialog1;
        saver.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (new File(s1).isDirectory()) {
                    String s2 = m + "/" + foldername.getText();
                    dirrenamer(s1, s2);
                } else {
                    String extension = s1.substring(s1.lastIndexOf("."));
                    String s2 = m + "/" + foldername.getText().toString() + extension;
                    Log.d("ok", s1 + "\n" + s2);
                    renamer(s1, s2);
                }
                customadaptor();
                Toast.makeText(getContext(), "File Renamed Successfuly", Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
        });
    }

    private void dialog5(final String m, final String n) {
        AlertDialog.Builder mkfolder = new AlertDialog.Builder(getActivity());
        @SuppressLint("InflateParams") View dialog = getLayoutInflater().inflate(R.layout.dialogframe2, null);
        final ListView foldername = dialog.findViewById(R.id.listview1);

        filefinder2(defaultpath);

        customadaptor2(foldername);

        foldername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                File[] file = new File[1];
                file[0] = listOfFiles[position].getAbsoluteFile();
                listOfFiles = file;
                File file1 = new File(listOfFiles[0].getAbsolutePath());
                filefinder2(file1);
                customadaptor2(foldername);
            }
        });
        mkfolder.setView(dialog);
        final AlertDialog dialog1 = mkfolder.create();
        dialog1.show();
        Button save = dialog.findViewById(R.id.saver);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String path = listOfFiles[0].getParent();
                File f = new File(m + "/" + n);
                boolean ok = f.renameTo(new File(path + "/" + n));
                boolean ok2 = f.delete();
                if (ok) {
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                }
                if (ok2) {
                    Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                }
                customadaptor();
                dialog1.dismiss();
            }
        });

    }

    private void dirrenamer(String s1, String s2) {
        File s10 = new File(s1);
        if (s10.exists()) {
            boolean ok = s10.delete();
            if (ok) {
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
            }
        }
        File s20 = new File(s2);
        if (!s20.exists()) {
            boolean ok = s20.mkdir();
            if (ok) {
                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void renamer(String s1, String s2) {
        boolean ok = new File(s1).renameTo(new File(s2));
        if (ok) {
            Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
        }
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            assert is != null;
            is.close();
            assert os != null;
            os.close();
        }
    }

    private String[] main(File f) {

        String[] infos = new String[5];
        if (f.exists()) {
            String s = "Name : " + f.getName();
            infos[0] = s;
            s = "Path : " + f.getPath();
            infos[1] = s;
            s = "Size : " + f.length();
            infos[2] = s;
            s = "Read Premission : " + f.canRead();
            infos[3] = s;
            s = "Write Premission : " + f.canWrite();
            infos[4] = s;
        } else {
            infos[0] = "The File does not exist";
        }
        return infos;
    }

    private void checkStorageReadPermission() {

        if (ContextCompat.checkSelfPermission(
                Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d("permission : ", "granted");
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Required ")
                    .setMessage("You Cant Use Apps Full Functionality Until You Allow The Required Permission ")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                        }
                    })
                    .create()
                    .show();

        } else {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

    }

    private void checkStorageWritePermission() {

        if (ContextCompat.checkSelfPermission(
                Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.d("permission : ", "granted");
            maintask();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission Required ")
                    .setMessage("You Cant Use Apps Full Functionality Until You Allow The Required Permission ")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                    })
                    .create()
                    .show();

        } else {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }
    private void maintask() {
        registerForContextMenu(lister);

        defaultpath = new File(Environment.getExternalStorageDirectory().getPath());


        dashboardViewModel.getList().setValue(lister);
        dashboardViewModel.getList().observe(getViewLifecycleOwner(), new Observer<ListView>() {
            @Override
            public void onChanged(ListView listView) {
                if (Objects.equals(dashboardViewModel.getFlag().getValue(), 0)) {
                    filefinder(defaultpath);
                } else {
                    listOfFiles = dashboardViewModel.getFile().getValue();
                    assert listOfFiles != null;
                    filefinder(Objects.requireNonNull(listOfFiles[0].getParentFile()));
                }
                customadaptor();
            }
        });

        lister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listOfFiles = dashboardViewModel.getFile().getValue();
                assert listOfFiles != null;
                if (listOfFiles[position].isDirectory()) {
                    filefinder(listOfFiles[position]);
                    customadaptor();

                } else {
                    fileopener(listOfFiles[position]);
                }
            }
        });
    }
}