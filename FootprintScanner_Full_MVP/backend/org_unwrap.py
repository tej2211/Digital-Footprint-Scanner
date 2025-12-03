# Org-side recovery script (conceptual). This assumes org has private keyset JSON and Tink installed.
from tink import hybrid
from tink import cleartext_keyset_handle
import base64, json
import sys

def main(packet_path: str, org_private_keyset_json: str):
    hybrid.register()
    handle = cleartext_keyset_handle.read_json_keyset(org_private_keyset_json)
    hybrid_decrypt = handle.primitive(hybrid.HybridDecrypt)
    with open(packet_path, "r") as f:
        packet = json.load(f)
    wrapped_for_org = base64.b64decode(packet['wrapped_for_org'])
    data_key = hybrid_decrypt.decrypt(wrapped_for_org, None)
    print("Recovered data key (hex):", data_key.hex())

if __name__ == '__main__':
    if len(sys.argv) < 3:
        print("Usage: python org_unwrap.py <packet.json> <org_private_keyset.json>")
        sys.exit(1)
    main(sys.argv[1], sys.argv[2])
