#!/bin/bash
echo "please choose user 'julie' or 'usrda':"
read user
echo "please input the password: "
read password
curl --cacert /home/ec2-user/grantguo/ca/caroot.cer --user $user:$password https://localhost:9999