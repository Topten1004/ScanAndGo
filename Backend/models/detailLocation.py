from start import db
from sqlalchemy import TEXT

class DetailLocationModel(db.Model):
    __tablename__ = 'detail_locations'
        
    id = db.Column(db.Integer, primary_key = True)
    floor_id = db.Column(db.Integer, db.ForeignKey('floors.id'))
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
                'floor_id': x.floor_id,
                'name': x.name,
                'img_data': x.img_data
            }
        return list(map(
            lambda x: to_json(x), 
            DetailLocationModel.query
            .filter(DetailLocationModel.floor_id == id)
            .order_by(DetailLocationModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'floor_id': x.floor_id,
                'name': x.name,
                'img_data': x.img_data
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

            return {'message': 'success!'}
        
        except:
            return {'message': 'error'}
    
    @classmethod
    def update_one(cls, id, name, img_data):   
        try:
            record = cls.query.get(id)
            # Ensure the record is found before accessing its attributes
            if record:
                record.name = name
                record.img_data = img_data
                db.session.commit()

                return {'message': 'success!'}

            else:
                return {'message': 'Record not found'}
        except Exception as e:
            print(e)
            return {'message': 'Error updating detail location'}