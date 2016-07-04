# 多线程下载图片



---
    //创建线程池
    private Executor threadPool = Executors.newFixedThreadPool(n);
    ps：n代表线程数
    
    //每个线程下载数
    for(int i = 0;i < 3;i++){
                long start = i*block;
                long end = (i+1)*block-1;
                if(i==2){
                    end=count;
                }
                DownLoadRunnable runnable = new DownLoadRunnable(url,fileDownLoad.getAbsolutePath(),start,end,handler);
                threadPool.execute(runnable);
            }




