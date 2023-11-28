from start import db
from sqlalchemy.orm import relationship


class CategoryModel(db.Model):
    __tablename__ = 'categories'
        
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(120), unique = True, nullable = False)

    inventory_category_entries = relationship('InventoryModel', back_populates='category')
    
    def save_to_db(self):
        db.session.add(self)
        db.session.commit()
        
    @classmethod
    def find_by_name(cls, name):
        return CategoryModel.query.filter_by(name = name).first()
        
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'name': x.name
            }
        return list(map(lambda x: to_json(x), CategoryModel.query.order_by(CategoryModel.id).all()))

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