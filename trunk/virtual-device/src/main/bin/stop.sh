#!/bin/bash
port=`grep -P "^port=" ../etc/configure-site.ini | sed "s/[a-z=]//g"`
echo "curl 'http://localhost:${port}/action=stop'"
curl "http://localhost:${port}/action=stop"
