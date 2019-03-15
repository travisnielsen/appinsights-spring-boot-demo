# Create a self-signed certificate

openssl req -x509 -newkey rsa:4096 -keyout private-key.pem -out cert.pem -days 365 -subj '/CN=social.contoso.com'
openssl pkcs12 -export -in cert.pem -inkey private-key.pem -out social-contoso-com.pfx

# get an unencrypted copy of the key
openssl rsa -in private-key.pem -out private.key

# convert the cert PEM file to CRT format
openssl x509 -outform der -in cert.pem -out social-contoso-com.crt

# convert the cert CRT file to PEM format
openssl x509 -inform der -in social-contoso-com.crt -out social-contoso-com.pem

# print key and certificate on one line for use in YAML files
awk '{printf "%s\\n", $0}' private-key.pem
awk '{printf "%s\\n", $0}' private.key

# create cert signed by a root CA
openssl genrsa -out social-contoso-com.key 4096
openssl req -new -key social-contoso-com.key -sha256 -nodes -subj '/CN=social.contoso.com/subjectAltName=DNS.1=social.contoso.com' -out social-contoso-com.csr

# sign the request
openssl x509 -req -in social-contoso-com.csr -CA rootCA.pem -CAkey /Users/travis/Documents/certs/trnielRootCA.key -out social-contoso-com.crt -days 1825 -sha256 -extfile san-extension2.cnf

# package as a PFX file, if necessary
openssl pkcs12 -export -in rxrapim-proxy.crt -inkey rxrapim-proxy.key -out rxrapim-proxy.pfx 

# Add the secret to k8s
kubectl create secret tls social-contoso-com --cert=cert.pem --key=private.key --namespace social-network
kubectl create secret tls social-contoso-com --cert=social-contoso-com.crt --key=social-contoso-com.key --namespace social-network