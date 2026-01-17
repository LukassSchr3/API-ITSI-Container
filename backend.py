from http.server import HTTPServer, BaseHTTPRequestHandler
import json
from datetime import datetime

class RequestLogger(BaseHTTPRequestHandler):
    def log_request_details(self, method):
        """Zeigt Details der Anfrage im Terminal"""
        print("\n" + "="*60)
        print(f"[{datetime.now().strftime('%Y-%m-%d %H:%M:%S')}]")
        print(f"Methode: {method}")
        print(f"Pfad: {self.path}")
        print(f"Client: {self.client_address[0]}:{self.client_address[1]}")
        print(f"Headers:")
        for header, value in self.headers.items():
            print(f"  {header}: {value}")
        
        # Body auslesen wenn vorhanden
        content_length = self.headers.get('Content-Length')
        if content_length:
            body = self.rfile.read(int(content_length))
            print(f"\nBody ({len(body)} bytes):")
            try:
                # Versuche als UTF-8 Text zu dekodieren
                body_text = body.decode('utf-8')
                print(body_text)
                # Versuche als JSON zu formatieren
                try:
                    json_obj = json.loads(body_text)
                    print("\nFormatiert als JSON:")
                    print(json.dumps(json_obj, indent=2, ensure_ascii=False))
                except:
                    pass
            except:
                print(f"[Binärdaten: {body[:100]}...]")
        
        print("="*60)
    
    def send_json_response(self, status=200):
        """Sendet eine JSON-Antwort zurück"""
        response = {
            "status": "success",
            "message": "Request logged",
            "timestamp": datetime.now().isoformat()
        }
        self.send_response(status)
        self.send_header('Content-type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        self.wfile.write(json.dumps(response).encode('utf-8'))
    
    def do_OPTIONS(self):
        """Behandelt CORS Preflight Requests"""
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, PATCH, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type, Authorization')
        self.end_headers()
    
    def do_GET(self):
        self.log_request_details("GET")
        self.send_json_response()
    
    def do_POST(self):
        self.log_request_details("POST")
        self.send_json_response()
    
    def do_PUT(self):
        self.log_request_details("PUT")
        self.send_json_response()
    
    def do_DELETE(self):
        self.log_request_details("DELETE")
        self.send_json_response()
    
    def do_PATCH(self):
        self.log_request_details("PATCH")
        self.send_json_response()
    
    def log_message(self, format, *args):
        # Unterdrückt die Standard-Log-Ausgaben
        pass

def run_server(port=3030):
    server_address = ('', port)
    httpd = HTTPServer(server_address, RequestLogger)
    print(f"Server läuft auf http://localhost:{port}")
    print("Warte auf Anfragen... (Strg+C zum Beenden)\n")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        print("\n\nServer wird beendet...")
        httpd.shutdown()

if __name__ == '__main__':
    run_server()