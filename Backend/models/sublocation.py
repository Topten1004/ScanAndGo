from start import db
from sqlalchemy import TEXT

class SubLocationModel(db.Model):
    __tablename__ = 'sublocations'
        
    id = db.Column(db.Integer, primary_key = True)
    location_id = db.Column(db.Integer, nullable = False)
    name = db.Column(db.String(120), nullable = False)
    img_data = db.Column(TEXT)
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_id_name(cls, id, name):
        return cls.query.filter_by(location_id = id, name = name).first()
        
    @classmethod
    def return_all_by_id(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'locationId': x.location_id,
                'name': x.name,
                'imgData': x.img_data
            }
        return list(map(
            lambda x: to_json(x), 
            SubLocationModel.query
            .filter(SubLocationModel.location_id == id)
            .order_by(SubLocationModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'locationId': x.location_id,
                'name': x.name
            }
        return list(map(
            lambda x: to_json(x), 
            SubLocationModel.query
            .order_by(SubLocationModel.id)
            .all()
        ))

    @classmethod
    def delete_one(cls, id):
        try:
            row_deleted = cls.query.filter_by(id=id).first()
            db.session.delete(row_deleted)
            db.session.commit()
        except:
            return {'message': 'error'}
    
    @classmethod
    def update_one(cls, id, name):
        try:
            record = cls.query.get(id)
            record.name = name
            db.session.commit()
        except:
            return {'message': 'error'}