from start import app
from flask_cors import CORS

from routes.index import Route_index

CORS(app, resources={r"/*":{"origins":"*"}})
Route_index(app)

@app.route("/")
def index():
    return "hello world"

if (__name__ == "__main__"):
    app.run(host='localhost', port=5000, debug=True)
    
    # , ssl_context='adhoc'

