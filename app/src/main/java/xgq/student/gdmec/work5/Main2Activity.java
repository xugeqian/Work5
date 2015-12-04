package xgq.student.gdmec.work5;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Vector;

/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class Main2Activity extends Activity {
    private final String[] FILE_MapTable = {".3gp",".mov",".avi",".rmvb",".wmv",".mp3",".mp4"};
    private Vector<String> items = null;
    private Vector<String> paths = null;
    private Vector<String> sizes = null;
    private String rootPath="/mnt/sdcard";
    private EditText pathEditText;
    private Button queryButton;
    private ListView fileListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("多媒体文件浏览");
        setContentView(R.layout.activity_main2);
        pathEditText = (EditText)findViewById(R.id.path_edit);
        queryButton = (Button)findViewById(R.id.qry_button);
        fileListView = (ListView) findViewById(R.id.file_listview);

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(pathEditText.getText().toString());
                if(file.exists()){
                    if(file.isFile()){
                        openFile(pathEditText.getText().toString());
                    }else{
                        getFileDir(pathEditText.getText().toString());
                    }
                }else{
                    Toast.makeText(Main2Activity.this,"找不到该位置，请确定位置是否正确！",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fileOrDir(paths.get(position));
            }
        });
        getFileDir(rootPath);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            pathEditText = (EditText)findViewById(R.id.path_edit);
            File file = new File(pathEditText.getText().toString());
            if(rootPath.equals(pathEditText.getText().toString().trim())){
                return  super.onKeyDown(keyCode,event);
            }else{
                getFileDir(file.getParent());
                return  true;
            }
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private void  fileOrDir(String path){
        File file = new File(path);
        if(file.isDirectory()){
            getFileDir(file.getPath());
        }else{
            openFile(path);
        }
    }
    private void getFileDir(String filePath){
        pathEditText.setText(filePath);
        items = new Vector<String>();
        paths = new Vector<String>();
        sizes = new Vector<String>();

        File f = new File(filePath);
        File[] files = f.listFiles();
        if(files!=null){
            for (int i=0;i<files.length;i++){
                if(files[i].isDirectory()) {
                    items.add(files[i].getName());
                    paths.add(files[i].getPath());
                    sizes.add("");
                }
            }
        }
        for(int i=0;i<files.length;i++){
            if(files[i].isFile()){
                String fileName = files[i].getName();
                int index = fileName.lastIndexOf(".");
                if(index>0){
                    String endName = fileName.substring(index,fileName.length()).toLowerCase();
                    String type = null;
                    for(int x=0;x<FILE_MapTable.length;x++){
                        if(endName.equals(FILE_MapTable[x])){
                            type = FILE_MapTable[x];
                            break;
                        }
                    }
                    if(type != null){
                        items.add(files[i].getName());
                        paths.add(files[i].getPath());
                        sizes.add(files[i].length()+"");
                    }
                }
            }
        }
        fileListView.setAdapter(new FileListAdapter(this,items));
    }
    private void openFile(String path){
        Intent intent = new Intent(Main2Activity.this,MainActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
        finish();
    }
    class FileListAdapter extends BaseAdapter
    {
        private Vector<String> items = null;
        private Main2Activity myFile;
        public FileListAdapter(Main2Activity myFile,Vector<String> items){
            this.items=items;
            this.myFile=myFile;
        }
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.elementAt(position);
        }

        @Override
        public long getItemId(int position) {
            return items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView = myFile.getLayoutInflater().inflate(R.layout.file_item,null);
            }
            TextView name = (TextView)convertView.findViewById(R.id.name);
            ImageView music = (ImageView)convertView.findViewById(R.id.music);
            ImageView floder = (ImageView)convertView.findViewById(R.id.floder);
            name.setText(items.elementAt(position));
            if(sizes.elementAt(position).equals("")){
                music.setVisibility(View.GONE);
                floder.setVisibility(View.VISIBLE);
            }else{
                floder.setVisibility(View.GONE);
                music.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }
}
