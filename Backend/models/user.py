from start import db
from passlib.hash import pbkdf2_sha256 as sha256
from models.log import LogModel

class UserModel(db.Model):
    __tablename__ = 'users'

    id = db.Column(db.Integer, primary_key = True)
    username = db.Column(db.String(120), unique = True, nullable = False)
    password = db.Column(db.String(120), nullable = False)
    role = db.Column(db.Integer, default = 0)

    logs = db.relationship('LogModel', back_populates='user')
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def find_by_username(cls, username):
        return cls.query.filter_by(username = username).first()

    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'username': x.username,
                'role': x.role
            }
        return {'users': list(map(lambda x: to_json(x), UserModel.query.filter(UserModel.role != 1)))}

    @classmethod
    def delete_all(cls):
        try:
            num_rows_deleted = db.session.query(cls).delete()
            db.session.commit()
            return {'message': '{} row(s) deleted'.format(num_rows_deleted)}
        except:
            return {'message': 'Something went wrong'}
    
    @staticmethod
    def generate_hash(password):
        return sha256.hash(password)
    
    @staticmethod
    def verify_password(password, hash):
        return sha256.verify(password, hash)
    
    @classmethod
    def update_one(cls, id, role):
        try:
            record = cls.query.get(id)
            record.role = role
            db.session.commit()
        except:
            return {'message': 'error'}
        
    @classmethod
    def delete_one(cls, id):
        try:
            row_deleted = cls.query.filter_by(id=id).first()
            db.session.delete(row_deleted)
            db.session.commit()
        except:
            return {'message': 'error'}
