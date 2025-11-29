from flask import Flask, request
import json

app = Flask(__name__)

@app.route('/', defaults={'path': ''}, methods=['GET', 'POST', 'PUT', 'DELETE', 'PATCH'])
@app.route('/<path:path>', methods=['GET', 'POST', 'PUT', 'DELETE', 'PATCH'])
def catch_all(path):
    print("\n" + "="*60)
    print(f"Method: {request.method}")
    print(f"Path: /{path}")
    print(f"Headers: {dict(request.headers)}")
    
    if request.data:
        try:
            print(f"Body (JSON): {json.loads(request.data)}")
        except:
            print(f"Body (Raw): {request.data.decode('utf-8')}")
    
    print("="*60 + "\n")
    
    # Return a simple success response
    return {"success": True, "containerId": "test123"}, 200

if __name__ == '__main__':
    print("Test Backend Server starting on port 3030...")
    print("This will print all HTTP requests received.")
    print("-" * 60)
    app.run(host='0.0.0.0', port=3030, debug=False)
