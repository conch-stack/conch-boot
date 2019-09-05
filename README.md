beihu-boot


docker run -d -p 4000:9000 --name biz_minio \
  -e "MINIO_ACCESS_KEY=root" \
  -e "MINIO_SECRET_KEY=beihu#2019#minio" \
  -v /root/data/minio/data:/data \
  -v /root/data/minio/config:/root/.minio \
  --restart=always \
  minio/minio server /data
  
  
永久链接
    policy命令 - 管理存储桶策略
    管理匿名访问存储桶和其内部内容的策略。
    
    Copy用法：
      mc policy [FLAGS] PERMISSION TARGET
      mc policy [FLAGS] TARGET
      mc policy list [FLAGS] TARGET
    
    PERMISSION:
      Allowed policies are: [none, download, upload, public].
    
    FLAGS:
      --help, -h                       显示帮助。  