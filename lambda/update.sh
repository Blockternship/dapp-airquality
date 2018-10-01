lambda_name=$1
zip_file="tx.zip"
aws lambda update-function-code \
--profile airq \
--region us-east-1 \
--function-name "${lambda_name}"  \
--zip-file "fileb://${zip_file}" 
