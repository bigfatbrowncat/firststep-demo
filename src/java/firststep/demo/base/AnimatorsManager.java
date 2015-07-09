package firststep.demo.base;

import java.util.LinkedList;

import firststep.Canvas;
import firststep.demo.base.Animator.Aftermath;

public class AnimatorsManager {
	private LinkedList<Animator> animators = new LinkedList<>();
	
	public void doFrame(Canvas cnv, float currentTime) {
		
		LinkedList<Animator> obsoletes = new LinkedList<Animator>();
		for (Animator anim : animators) {
			anim.doFrame(cnv, currentTime);
			if (!anim.isActual(currentTime) && anim.getAftermath() == Aftermath.REMOVE) {
				obsoletes.add(anim);
			}
		}
		animators.removeAll(obsoletes);
		
	}
	
	public void addAnimator(Animator animator) {
		animators.add(animator);
	}
	
	public void removeAnimator(Animator animator) {
		animators.remove(animator);
	}
	
	public void clear() {
		animators.clear();
	}
}
