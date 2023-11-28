from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_jwt_extended import JWTManager

app = Flask(__name__)

# jwt-passport
app.config['JWT_SECRET_KEY'] = 'secret!!!'
jwt = JWTManager(app)

# connect to the MySql
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:''@localhost:3306/rfid'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

@app.before_request
def create_tables():
    db.create_all()
