#!/bin/sh

openssl aes-256-cbc -K $encrypted_f6f7690d4c17_key -iv $encrypted_f6f7690d4c17_iv -in travis-deploy-key.enc -out travis-deploy-key -d;
chmod 600 travis-deploy-key;
cp travis-deploy-key ~/.ssh/id_rsa;
