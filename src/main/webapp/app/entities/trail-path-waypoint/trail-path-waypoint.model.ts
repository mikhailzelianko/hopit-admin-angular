import { ITrailPath } from 'app/entities/trail-path/trail-path.model';

export interface ITrailPathWaypoint {
  id?: number;
  waypointLat?: number | null;
  waypointLong?: number | null;
  trailpath?: ITrailPath | null;
}

export class TrailPathWaypoint implements ITrailPathWaypoint {
  constructor(
    public id?: number,
    public waypointLat?: number | null,
    public waypointLong?: number | null,
    public trailpath?: ITrailPath | null
  ) {}
}

export function getTrailPathWaypointIdentifier(trailPathWaypoint: ITrailPathWaypoint): number | undefined {
  return trailPathWaypoint.id;
}
