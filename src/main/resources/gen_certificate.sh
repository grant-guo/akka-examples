#!/bin/bash

export PW=`cat password`

echo "generating the key pair and save to the keystore"
keytool -genkeypair \
    -alias xuexu \
    -keystore xuexu.jks \
    -dname "CN=localhost, OU=Trust Authority, O=Schedule1, L=Toronto, ST=Ontario, C=CA" \
    -keypass $PW \
    -storepass $PW \
    -keyalg RSA \
    -keysize 4096 \
    -ext KeyUsage:critical="keyCertSign" \
    -ext BasicConstraints:critical="ca:true" \
    -sigalg SHA1withRSA

echo "self-signing the certificate where both owner and issuer are the same as xuexu.jks"
keytool -export -alias xuexu -file xuexu_self_signed.cer -keystore xuexu.jks -storepass $PW

echo "generating the certificate signing request(CSR) for xuexu.jks"
keytool -certreq -alias xuexu -keystore xuexu.jks -file xuexu.csr -storepass $PW

set RANDFILE=rand

echo "generating CA's private key and CA's CSR"
openssl req -new -keyout cakey.pem -out careq.pem #-config /usr/lib/ssl/openssl.cnf

echo "self-signing CA's certificate"
openssl x509 -signkey cakey.pem -req -days 3650 -in careq.pem -out caroot.cer -extensions v3_ca

echo 1234 > serial.txt

echo "signing xuexu.csr using CA's certificate and CA's private key"
openssl x509 \
    -CA caroot.cer \
    -CAkey cakey.pem \
    -CAserial serial.txt \
    -req \
    -in xuexu.csr \
    -out xuexu_signed_by_CA.cer
    -days 365

echo "adding CA's certificate to the keystore"
keytool -import -alias schedule1 -file caroot.cer -keystore xuexu.jks -storepass $PW

echo "adding xuexu's certificate signed by CA to the keystore"
keytool -import -alias xuexu -file xuexu_signed_by_CA.cer -keystore xuexu.jks -storepass $PW


