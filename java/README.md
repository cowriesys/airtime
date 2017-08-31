Compile with
`javac src\AirtimeClient.java -d bin -cp lib\javax.json-1.0.4.jar;lib\commons-codec-1.10.jar`

Run with
`java -cp bin;lib\javax.json-1.0.4.jar;lib\commons-codec-1.10.jar AirtimeClient MTN 2347038662037 100`

If you encounter SSL trust errors you may need to import the certificate with
```
keytool -trustcacerts -keystore "%java_home%\jre\lib\security\cacerts" -storepass changeit -importcert -file cert\27569466a96936ff0389fcec77bb3a473097318a.pem
```