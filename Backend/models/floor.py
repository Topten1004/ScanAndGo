from start import db

class FloorModel(db.Model):
    __tablename__ = 'floors'
        
    id = db.Column(db.Integer, primary_key = True)
    area_id = db.Column(db.Integer, db.ForeignKey('areas.id'))
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
                'area_id': x.area_id,
                'name': x.name
            }
        return list(map(
            lambda x: to_json(x), 
            FloorModel.query
            .filter(FloorModel.area_id == id)
            .order_by(FloorModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'area_id': x.area_id,
                'name': x.name
            }
        return list(map(lambda x: to_json(x), FloorModel.query.order_by(FloorModel.id).all()))
    
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
    def update_one(cls, id, name, area_id):
        try:
            record = cls.query.get(id)
            record.name = name
            record.area_id = area_id
            db.session.commit()

            return {'message': 'success!'}

        except:
            return {'message': 'error'}