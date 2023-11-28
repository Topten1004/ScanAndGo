from start import db
from sqlalchemy.orm import relationship
import datetime

class LogModel(db.Model):
    __tablename__ = 'logs'

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    user_name = db.Column(db.String(120))
    login_date = db.Column(db.Date)

    # Define the relationship with the User model
    user = db.relationship('UserModel', back_populates='logs')
 
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_id_user_id(cls, user_id):
        return LogModel.query.filter_by(user_id = user_id).first()
    
    @classmethod
    def find_by_id(cls, id):
        return LogModel.query.filter_by(id = id).first()
        
    @classmethod
    def return_all_by_id(cls, user_id):
        def to_json(x):
            return {
                'id': x.id,
                'user_id': x.user_id,
                'user_name': x.user_name,
                'login_date': x.date,
            }
        return list(map(
            lambda x: to_json(x), 
            LogModel.query
            .filter(LogModel.user_id == user_id)
            .order_by(LogModel.id)
            .all()
        ))

    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'user_id': x.user_id,
                'user_name': x.user_name,
                'login_date': x.date
            }
        return list(map(
            lambda x: to_json(x), 
            LogModel.query
            .order_by(LogModel.id)
            .all()
        ))


