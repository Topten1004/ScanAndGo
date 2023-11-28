from start import db

class RoleModel(db.Model):
    __tablename__ = 'role'
        
    id = db.Column(db.Integer, primary_key = True)
    name = db.Column(db.String(120), unique = True, nullable = False)
    
    @classmethod
    def return_all(cls):
        def to_json(x):
            return {
                'id': x.id,
                'name': x.name
            }
        return list(map(lambda x: to_json(x), RoleModel.query.order_by(RoleModel.id).all()))