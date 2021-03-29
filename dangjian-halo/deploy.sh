#!/bin/bash

# 指定上研党建小程序的根目录，请按实际修改
HALO_DIR="/www/wwwroot/halo"

# 拉取最新的源码
# git pull

# 进入上研党建小程序根目录
cd $HALO_DIR

# 停止上研党建小程序
sh $HALO_DIR/bin/halo.sh stop

# 执行打包
mvn package -Pprod

# 进入打包好的上研党建小程序目录
cd $HALO_DIR/target/dist/halo

# 运行上研党建小程序
nohup java -server -Xms256m -Xmx512m -jar `find ./ -name "halo*.jar"` > /dev/null 2>&1 &

echo "上研党建小程序部署完毕，Enjoy！"