from start import db
from sqlalchemy import TEXT

class DetailLocationModel(db.Model):
    __tablename__ = 'detail_locations'
        
    id = db.Column(db.Integer, primary_key = True)
    floorId = db.Column(db.Integer, db.ForeignKey('floors.id'))
    name = db.Column(db.String(120), nullable = False)
    img_data = db.Column(TEXT)
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_id_name(cls, id, name):
        return cls.query.filter_by(id = id, name = name).first()
        
    @classmethod
    def return_all_by_id(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'floorId': x.floorId,
                'name': x.name,
                'imgData': x.img_data
            }
        return list(map(
            lambda x: to_json(x), 
            DetailLocationModel.query
            .filter(DetailLocationModel.floorId == id)
            .order_by(DetailLocationModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'floorId': x.floorId,
                'name': x.name,
                'imgData': x.img_data
            }
        return list(map(
            lambda x: to_json(x), 
            DetailLocationModel.query
            .order_by(DetailLocationModel.id)
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