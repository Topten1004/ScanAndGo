from start import db

class AreaModel(db.Model):
    __tablename__ = 'areas'
        
    id = db.Column(db.Integer, primary_key = True)
    building_id = db.Column(db.Integer, db.ForeignKey('buildings.id'))
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
                'building_id': x.building_id,
                'name': x.name
            }
        return list(map(
            lambda x: to_json(x), 
            AreaModel.query
            .filter(AreaModel.building_id == id)
            .order_by(AreaModel.id)
            .all()
        ))
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'building_id': x.building_id,
                'name': x.name
            }
        return list(map(lambda x: to_json(x), AreaModel.query.order_by(AreaModel.id).all()))

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
    def update_one(cls, id, name, building_id):
        try:
            record = cls.query.get(id)
            record.name = name
            record.building_id = building_id

            db.session.commit()

            return {'message': 'success!'}
        except:
            return {'message': 'error'}