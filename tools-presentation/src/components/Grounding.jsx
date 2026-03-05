import BottomNav from './BottomNav.jsx'
import WellDoneSlide from './WellDoneSlide.jsx'

const STEPS = [
  { number: '5', phrase: 'things you can see', instruction: 'Name 5 things you can see around you right now', icon: '👁' },
  { number: '4', phrase: 'things you can touch', instruction: 'Name 4 things you can physically feel or touch', icon: '✋' },
  { number: '3', phrase: 'things you can hear', instruction: 'Name 3 sounds you can hear in your environment', icon: '👂' },
  { number: '2', phrase: 'things you can smell', instruction: 'Name 2 things you can smell (or like to smell)', icon: '👃' },
  { number: '1', phrase: 'things you can taste', instruction: 'Name 1 thing you can taste right now', icon: '👅' },
]

export default function Grounding({ currentSlide, onNext, onPrevious }) {
  if (currentSlide >= STEPS.length) {
    return <WellDoneSlide exerciseName="Grounding" onBackToTools={onNext} />
  }

  const step = STEPS[currentSlide]
  const stepIndex = currentSlide + 1

  return (
    <div className="bg-bg-splash min-h-screen flex flex-col">
      <div className="flex-1 px-6 pt-7">
        <button type="button" className="text-desc-gray text-[16px] font-medium" onClick={onPrevious}>
          ‹ Back
        </button>

        <h1 className="text-title-blue text-[30px] font-extrabold mt-7 leading-tight">5-4-3-2-1 Grounding</h1>
        <p className="text-desc-gray text-[14px] mt-2">Bring yourself back to the present moment</p>

        <div className="flex gap-3 mt-6">
          {[1, 2, 3, 4, 5].map((i) => (
            <div
              key={i}
              className={`h-2 rounded-full ${i <= stepIndex ? 'bg-button-blue' : 'bg-stroke-gray/60'} ${i <= stepIndex ? 'w-[76px]' : 'w-[76px]'}`}
            />
          ))}
        </div>

        <div className="flex flex-col items-center mt-12">
          <div className="w-[92px] h-[92px] rounded-[26px] bg-gradient-to-br from-[#B3E5FC] to-[#D1C4E9] flex items-center justify-center shadow-[0_12px_28px_rgba(26,35,126,0.08)]">
            <span className="text-[34px]">{step.icon}</span>
          </div>

          <div className="w-full bg-white rounded-[22px] px-8 py-8 mt-7 border border-stroke-gray/40 shadow-[0_10px_22px_rgba(26,35,126,0.06)] text-center">
            <div className="text-button-blue text-[56px] font-extrabold leading-none">{step.number}</div>
            <div className="text-title-blue text-[20px] font-extrabold mt-2">{step.phrase}</div>
            <div className="text-desc-gray text-[15px] mt-4 leading-6 max-w-[260px] mx-auto">{step.instruction}</div>
          </div>

          <div className="text-desc-gray/70 text-[12px] mt-7">Step {stepIndex} of 5</div>

          {currentSlide === 0 ? (
            <button
              type="button"
              onClick={onNext}
              className="w-full h-[58px] rounded-2xl bg-button-blue text-white text-[18px] font-bold mt-12 shadow-[0_14px_28px_rgba(74,144,226,0.35)]"
            >
              Next Step
            </button>
          ) : (
            <div className="flex gap-4 w-full mt-10">
              <button
                type="button"
                onClick={onPrevious}
                className="flex-1 h-[58px] rounded-2xl bg-white/60 border border-stroke-gray/60 text-desc-gray text-[16px] font-semibold"
              >
                Previous
              </button>
              <button
                type="button"
                onClick={onNext}
                className="flex-1 h-[58px] rounded-2xl bg-button-blue text-white text-[16px] font-bold shadow-[0_14px_28px_rgba(74,144,226,0.35)]"
              >
                {currentSlide === STEPS.length - 1 ? 'Complete ✓' : 'Next Step'}
              </button>
            </div>
          )}
        </div>
      </div>

      <BottomNav />
    </div>
  )
}
