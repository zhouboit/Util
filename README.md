# Https_JAVA
java 使用证书请求https 测试

在进行测试之前需要本地导入根证书

导入证书：

keytool -import -keystore "D:\env\Java\jdk1.8.0_201\jre\lib\security\cacerts"  -storepass changeit ~~-keypass XXXXXX~~ -alias aliasName -file C:\Users\bo.zhou1.XXXX\Documents\api证书20190709\rootca.crt

keytool：jre下的工具，配置环境变量后可以直接使用，如果没有配置环境变量或者本机存在多个jre需要使用绝对路径

keystore：本地jre存储安全证书的地方 绝对路径 "D:\env\Java\jdk1.8.0_201\jre\lib\security\cacerts"  

storepass：本地证书库的密码，默认 changeit 

 ~~keypass：要导入的证书秘钥 XXXXXX ~~

alias：证书别名，方便在 证书库中查看识别 api0.hirain.com 

file：证书的存储路径 C:\Users\bo.zhou1.XXXX\Documents\api证书20190709\rootca.crt

查看证书：

keytool -list -keystore "D:\env\Java\jdk1.8.0_201\jre\lib\security\cacerts"  -storepass changeit #查看所有

keytool -list -keystore "D:\env\Java\jdk1.8.0_201\jre\lib\security\cacerts"  -storepass changeit -alias aliasName #查看指定别名的证书信息

删除证书：

keytool -delete -keystore "D:\env\Java\jdk1.8.0_201\jre\lib\security\cacerts"  -storepass changeit -alias aliasName #删除指定别名的证书信息
