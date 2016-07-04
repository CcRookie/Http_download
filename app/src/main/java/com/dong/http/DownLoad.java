package com.dong.http;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by dong on 16-7-5.
 */
public class DownLoad {
    private File fileDownLoad;
    private Handler handler;

    public DownLoad(Handler handler){
        this.handler = handler;


    }
    //创建线程池
    private Executor threadPool = Executors.newFixedThreadPool(3);

    class DownLoadRunnable implements Runnable{
        private String url;
        private String fileName;
        private long start;
        private long end;
        private Handler handler;

        public DownLoadRunnable(String url,String fileName,long start,long end,Handler handler){
            this.url = url;
            this.fileName = fileName;
            this.start = start;
            this.end = end;
            this.handler = handler;
        }
        @Override
        public void run() {
            try {
                URL httpUrl = new URL(url);
                HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
                conn.setReadTimeout(5000);
                conn.setRequestProperty("Range","bytes="+start+"-"+end);
                conn.setRequestMethod("GET");
                RandomAccessFile acess = new RandomAccessFile(new File(fileName),"rwd");
                acess.seek(start);
                InputStream in = conn.getInputStream();
                byte[] b = new byte[1024*4];
                int len=0;
                while((len=in.read(b))!=-1){
                    acess.write(b,0,len);
                }
                if(acess!=null){
                    acess.close();
                }
                if(in!=null){
                    in.close();
                }

                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void downLoadFile(String url){
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            int count = conn.getContentLength();
            int block = count/3;
            String fileName = getFileName(url);
            File parent = Environment.getExternalStorageDirectory();
            fileDownLoad = new File(parent,fileName);
            for(int i = 0;i < 3;i++){
                long start = i*block;
                long end = (i+1)*block-1;
                if(i==2){
                    end=count;
                }
                DownLoadRunnable runnable = new DownLoadRunnable(url,fileDownLoad.getAbsolutePath(),start,end,handler);
                threadPool.execute(runnable);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String getFileName(String url){
        return url.substring(url.lastIndexOf("/")+1);
    }
}
