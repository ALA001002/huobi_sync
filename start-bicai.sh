ps -ef | awk 'kline-generate && !/awk/{print $2}'| xargs kill -9
git pull origin master
mvn install
nohup java -jar ./target/kline-generate-0.0.1-SNAPSHOT.jar --spring.profiles.active=bicai &

