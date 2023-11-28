from start import db

class FloorModel(db.Model):
    __tablename__ = 'floors'
        
    id = db.Column(db.Integer, primary_key = True)
    areaId = db.Column(db.Integer, db.ForeignKey('areas.id'))
    name = db.Column(db.String(120), nullable = False)
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_name(cls, name):
        return cls.query.filter_by(name = name).first()
    
    @classmethod
    def return_all_by_id(cls, id):
        def to_json(x):
            return {
                'id': x.id,
                'areaId': x.areaId,
                'name': x.name
            }
        return list(map(
            lambda x: to_json(x), 
            FloorModel.query
            .filter(FloorModel.areaId == id)
            .order_by(FloorModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'areaId': x.areaId,
                'name': x.name
            }

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