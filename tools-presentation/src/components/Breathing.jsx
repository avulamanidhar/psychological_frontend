import WellDoneSlide from './WellDoneSlide.jsx'
import BottomNav from './BottomNav.jsx'

/**
 * SECTION 5 – 4-7-8 BREATHING
 * Slide 0: Breathing ready screen
 * Slide 1: Breathing Well Done screen
 */
export default function Breathing({ currentSlide, onNext, onPrevious }) {
  if (currentSlide === 1) {
    return <WellDoneSlide exerciseName="Breathing" onBackToTools={onNext} />
  }

  return (
    <div className="bg-bg-splash min-h-screen flex flex-col">
      <div className="flex-1 px-6 pt-7">
        <button type="button" className="text-desc-gray text-[16px] font-medium" onClick={onPrevious}>
          ‹ Back
        </button>

        <h1 className="text-title-blue text-[34px] font-extrabold mt-7 leading-none">4-7-8 Breathing</h1>
        <p className="text-desc-gray text-[14px] mt-2">Inhale 4s · Hold 7s · Exhale 8s</p>

        <div className="flex flex-col items-center mt-16">
          <div className="relative w-[240px] h-[240px] flex items-center justify-center">
            <div className="absolute inset-0 rounded-full bg-[#B3E5FC]/20" />
            <div className="absolute inset-[18px] rounded-full bg-[#B3E5FC]/28" />
            <div className="absolute inset-[46px] rounded-full bg-[#B2DFDB]/65" />
            <div className="relative w-[120px] h-[120px] rounded-full bg-[#A5D6D3] flex items-center justify-center">
              <span className="text-white text-[18px] font-bold">Ready</span>
            </div>
          </div>

          <p className="text-desc-gray text-[15px] mt-10">Session: 0 cycles · 00:00</p>

          <div className="flex items-center gap-10 mt-9">
            <button type="button" className="w-[74px] h-[74px] rounded-full bg-button-blue text-white flex items-center justify-center shadow-[0_14px_28px_rgba(74,144,226,0.35)]">
              <span className="text-[22px] ml-1">▶</span>
            </button>
            <button type="button" className="w-[74px] h-[74px] rounded-full bg-white/70 border border-stroke-gray/60 text-desc-gray flex items-center justify-center">
              <span className="text-[26px]">↻</span>
            </button>
          </div>

          <button
            type="button"
            onClick={onNext}
            className="mt-12 w-full h-[58px] rounded-2xl border-2 border-button-blue text-desc-gray bg-transparent font-semibold shadow-[0_10px_18px_rgba(74,144,226,0.10)]"
          >
            Complete Session ✓
          </button>
        </div>
      </div>

      <BottomNav />
    </div>
  )
}
