#!/bin/bash

# Required Inputs
CLIENT_ID="<your-client-id>"
CLIENT_SECRET="<your-client-secret>"
TENANT_ID="<your-tenant-id>"
SCOPE="https://graph.microsoft.com/.default"
FROM_EMAIL="your_email@domain.com"
TO_EMAIL="recipient_email@domain.com"
REPORT_PATH="newman-report.html"

# Step 1: Get Access Token
echo "Fetching access token..."
TOKEN_RESPONSE=$(curl -s -X POST "https://login.microsoftonline.com/$TENANT_ID/oauth2/v2.0/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=$CLIENT_ID&scope=$SCOPE&client_secret=$CLIENT_SECRET&grant_type=client_credentials")

ACCESS_TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r .access_token)

# Step 2: Encode file to Base64
BASE64_CONTENT=$(base64 -w 0 "$REPORT_PATH")
FILE_NAME="newman-report.html"

# Step 3: Prepare JSON payload
read -r -d '' EMAIL_JSON <<EOF
{
  "message": {
    "subject": "Newman Test Report",
    "body": {
      "contentType": "HTML",
      "content": "Hi Team,<br><br>Attached is the Newman test report.<br><br>Regards,<br>Jenkins"
    },
    "toRecipients": [
      {
        "emailAddress": {
          "address": "$TO_EMAIL"
        }
      }
    ],
    "attachments": [
      {
        "@odata.type": "#microsoft.graph.fileAttachment",
        "name": "$FILE_NAME",
        "contentBytes": "$BASE64_CONTENT"
      }
    ]
  },
  "saveToSentItems": "false"
}
EOF

# Step 4: Send Email
echo "Sending email..."
curl -s -X POST "https://graph.microsoft.com/v1.0/users/$FROM_EMAIL/sendMail" \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d "$EMAIL_JSON"
